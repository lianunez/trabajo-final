function helloPanel() {
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
  let panel = document.getElementById("hello-panel");
  panel.innerHTML = `Hola 👋, ${userInfo.user}!`;
}
helloPanel();

getProducts();
async function getProducts() {
  let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
  try {
    const response = await productsFromAPI(userInfo);

    switch (response.status) {
      case 200:
        const products = await response.text().then(JSON.parse);
        setProductsTable(products);
        graphics(products);
        break;
      case 403:
        location.href = "./components/logout.html";
        break;
      case 500:
        console.error("Internal server error!");
        break;
    }
  } catch (error) {
    console.error(error);
  }
}

async function productsFromAPI(userInfo) {
  try {
    const response = await fetch(
      "http://127.0.0.1:8080/api/v1/products/check",
      {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "Trigger-User": userInfo.id,
          Authorization: `Bearer ${userInfo.jwt}`,
        },
      }
    );
    return response;
  } catch (error) {
    console.error(error);
  }
}

let currentPage = 1;
const rowsPerPage = 5; // Number of rows per page

function setProductsTable(products, page = 1) {
  console.log("table");
  const tableBody = document.getElementById("prod-table-body");
  tableBody.innerHTML = "";

  // Calculate pagination
  const start = (page - 1) * rowsPerPage;
  const end = start + rowsPerPage;
  const paginatedProducts = products.slice(start, end);

  paginatedProducts.forEach((product) => {
    const row = document.createElement("tr");

    if (product.criticalStock) {
      row.style.backgroundColor = "#DC3545"; // Alert red for critical stock
    }

    if (product.warningStock) {
      row.style.backgroundColor = "#EED202"; // Alert yellow for warning stock
    }

    row.innerHTML = `
        <td>${product.code}</td>
        <td>${product.name}</td>
        <td>${product.amount}</td>
        <td>${product.expiry_date}</td>
        <td>${product.user_id.user}</td>
        <td>${product.provider_id.name}</td>
        <td>
            <button class="btn btn-warning" onclick="openEditModal(${product.id})" data-permission="products.update">Edit</button>
        </td>
        <td>
            <button class="btn btn-danger" onclick="confirmDelete(${product.id})" data-permission="products.delete">Delete</button>
        </td>
    `;

    tableBody.appendChild(row);
  });

  updatePaginationButtons(products);
  handleDynamicTablePermissions(); // Apply permissions to the dynamic table
  handleStaticElementsPermissions();
}

function updatePaginationButtons(products) {
  const paginationContainer = document.getElementById("pagination");
  paginationContainer.innerHTML = "";

  const totalPages = Math.ceil(products.length / rowsPerPage);

  // Previous Button
  const prevButton = document.createElement("button");
  prevButton.innerText = "Previous";
  prevButton.className = "btn btn-primary";
  prevButton.disabled = currentPage === 1;
  prevButton.onclick = () => {
    if (currentPage > 1) {
      currentPage--;
      setProductsTable(products, currentPage);
    }
  };
  paginationContainer.appendChild(prevButton);

  // Page Number Buttons
  for (let i = 1; i <= totalPages; i++) {
    const button = document.createElement("button");
    button.innerText = i;
    button.className = "btn";
    button.onclick = () => {
      currentPage = i;
      setProductsTable(products, i);
    };

    if (i === currentPage) {
      button.style.fontWeight = "bold";
      button.style.color = "#DC3545";
    }

    paginationContainer.appendChild(button);
  }

  // Next Button
  const nextButton = document.createElement("button");
  nextButton.innerText = "Next";
  nextButton.className = "btn btn-primary";
  nextButton.disabled = currentPage === totalPages;
  nextButton.onclick = () => {
    if (currentPage < totalPages) {
      currentPage++;
      setProductsTable(products, currentPage);
    }
  };
  paginationContainer.appendChild(nextButton);
}

function editProduct(id) {
  console.log(id, "edit");
}

// <DELETE>
function confirmDelete(productId) {
  const isConfirmed = confirm("Estás seguro de eliminar este producto?");

  if (isConfirmed) {
    deleteProduct(productId);
  }
}

async function deleteProduct(id) {
  try {
    const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
    const response = await fetch(
      `http://127.0.0.1:8080/api/v1/products/delete/${id}`,
      {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          "Trigger-User": userInfo.id,
          Authorization: `Bearer ${userInfo?.jwt}`,
        },
      }
    );

    switch (response.status) {
      case 202:
        getProducts();
        break;
      case 403:
        location.href = "./components/logout.html";
        break;
      case 500:
        console.error("Internal server error!");
        break;
    }
  } catch (error) {
    log.error(error);
  }
}
// </DELETE>

// <EDIT>
async function openEditModal(productId) {
  let messageDiv = document.getElementById("after-process-msg");
  messageDiv.hidden = true;
  let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
  const response = await productsFromAPI(userInfo);
  const products = await response.json();
  const product = products.find((p) => p.id === productId);
  if (!product) return;

  // Fill form fields
  document.getElementById("editProductId").value = product.id;
  document.getElementById("editCode").value = product.code;
  document.getElementById("editName").value = product.name;
  document.getElementById("editAmount").value = product.amount;
  document.getElementById("editExpiryDate").value = product.expiry_date;
  document.getElementById("editUserId").value = product.user_id.id;
  document.getElementById("editProviderId").value = product.provider_id.id;

  // Show modal
  const modal = new bootstrap.Modal(
    document.getElementById("editProductModal")
  );
  modal.show();
}

async function updateProduct() {
  try {
    let userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
    const productId = document.getElementById("editProductId").value;
    const data = {
      id: productId,
      code: document.getElementById("editCode").value,
      name: document.getElementById("editName").value,
      amount: document.getElementById("editAmount").value,
      expiry_date: document.getElementById("editExpiryDate").value,
      user_id: document.getElementById("editUserId").value,
      provider_id: document.getElementById("editProviderId").value,
    };

    const response = await fetch(
      "http://127.0.0.1:8080/api/v1/products/update",
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Trigger-User": userInfo.id,
          Authorization: `Bearer ${userInfo?.jwt}`,
        },
        body: JSON.stringify(data),
      }
    );

    switch (response.status) {
      case 200:
        let messageDiv = document.getElementById("after-process-msg");
        messageDiv.hidden = false;
        messageDiv.classList = "mb-3 text-success";
        messageDiv.innerHTML = "Producto actualizado.";
        const modal = new bootstrap.Modal(
          document.getElementById("editProductModal")
        );
        modal.hide();
        getProducts();
        //messageDiv.style.display = "none";
        break;
      case 403:
        let messageField = document.getElementById("after-process-msg");
        messageField.hidden = false;
        messageField.classList = "mb-3 text-danger";
        messageField.innerHTML = "No tienes permisos para esta acción.";
        break;

      default:
        messageDiv.style.display = "block";
        messageDiv.classList = "mb-3 text-danger";
        messageDiv.innerHTML = "Algo salió mal.";
        //messageDiv.style.display = "none";
        break;
    }
  } catch (error) {
    console.error(error);
    let messageDiv = document.getElementById("after-process-msg");
    messageDiv.hidden = false;
    messageDiv.classList = "mb-3 text-danger";
    messageDiv.innerHTML = `Algo salió mal. ${error}`;
  }
}
// </EDIT>

const wt2 = new PerfectScrollbar(".widget-todo2");

function graphics(products) {
  "use strict";
  let labels = groupProviders(products);
  var data = {
    labels: labels.map((provider) => provider.name), // Extract provider names for labels
    series: labels.map((provider, index) => ({
      value: provider.count, // Use the count as the value
      className: getClassName(index), // Assign dynamic class names
    })),
  };

  var options = {
    labelInterpolationFnc: function (value) {
      return value[0];
    },
  };

  var responsiveOptions = [
    [
      "screen and (min-width: 640px)",
      {
        chartPadding: 30,
        labelOffset: 100,
        labelDirection: "explode",
        labelInterpolationFnc: function (value) {
          return value;
        },
      },
    ],
    [
      "screen and (min-width: 1024px)",
      {
        labelOffset: 80,
        chartPadding: 20,
      },
    ],
  ];

  new Chartist.Pie(".ct-pie-chart", data, options, responsiveOptions);

  /*----------------------------------*/

  var data = generateChartData(products);

  var options = {
    seriesBarDistance: 10,
  };

  var responsiveOptions = [
    [
      "screen and (max-width: 640px)",
      {
        seriesBarDistance: 5,
        axisX: {
          labelInterpolationFnc: function (value) {
            return value[0];
          },
        },
      },
    ],
  ];

  new Chartist.Bar(".ct-bar-chart", data, options, responsiveOptions);

  $(".year-calendar").pignoseCalendar({
    theme: "blue", // light, dark, blue
  });
}

function groupProviders(products) {
  const providerCount = {};

  products.forEach((product) => {
    const providerName = product.provider_id.name;
    providerCount[providerName] = (providerCount[providerName] || 0) + 1;
  });

  const providerList = Object.keys(providerCount).map((name) => ({
    name: name,
    count: providerCount[name],
  }));

  return providerList;
}

function generateChartData(products) {
  const grouped = groupProductsByMonth(products);
  const months = [
    "jan",
    "feb",
    "mar",
    "apr",
    "may",
    "jun",
    "jul",
    "aug",
    "sep",
    "oct",
    "nov",
    "dec",
  ];

  const labels = months.map(
    (month) => month.charAt(0).toUpperCase() + month.slice(1)
  );

  const series = months.map((month) =>
    grouped[month] ? grouped[month].count : 0
  );

  return {
    labels: labels,
    series: [series],
  };
}

function groupProductsByMonth(products) {
  const monthMap = {};

  products.forEach((product) => {
    const date = new Date(product.date);
    const month = date
      .toLocaleString("en-US", { month: "short" })
      .toLowerCase(); // "feb"

    if (!monthMap[month]) {
      monthMap[month] = { count: 0, products: [] };
    }

    monthMap[month].products.push(product);

    monthMap[month].count += 1;
  });

  return monthMap;
}

function getClassName(index) {
  const classes = ["bg-facebook", "bg-twitter", "bg-youtube", "bg-google-plus"];
  return classes[index % classes.length];
}

//roles
function handleDynamicTablePermissions() {
  console.log("handleDynamicTablePermissions");
  // Get the user permissions from session storage
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
  if (
    !userInfo ||
    !userInfo.role ||
    !Array.isArray(userInfo.role.permissions)
  ) {
    console.error("Invalid userInfo structure:", userInfo);
    return;
  }

  const permissions = userInfo.role.permissions;

  // Loop through all the edit and delete buttons in the dynamic table
  const actionButtons = document.querySelectorAll("[data-permission]"); // Target elements with data-permission attribute

  actionButtons.forEach((button) => {
    const actionId = button.getAttribute("data-permission");

    if (actionId && !permissions.includes(actionId)) {
      button.style.display = "none"; // Hide button if permission is not found
    }
  });
}

function handleStaticElementsPermissions() {
  console.log("handleStaticElementsPermissions");
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
  if (
    !userInfo ||
    !userInfo.role ||
    !Array.isArray(userInfo.role.permissions)
  ) {
    console.error("Invalid userInfo structure:", userInfo);
    return;
  }

  const actions = [
    document.getElementById("products.create"),
    document.getElementById("providers.create"),
    document.getElementById("products.check"),
    document.getElementById("users.create"),
    document.getElementById("transfers.create"),
    document.getElementById("transfers.check"),
  ];

  const permissions = userInfo.role.permissions;
  console.log(permissions);

  actions.forEach((action) => {
    if (action) {
      const actionId = action.id;
      if (!permissions.includes(actionId)) {
        action.style.display = "none";
      }
    }
  });
}

async function exportAllProductsToExcel() {
  try {
    const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
    if (!userInfo || !userInfo.jwt || !userInfo.id) {
      console.error("User not logged in or missing JWT!");
      return;
    }

    const response = await productsFromAPI(userInfo);
    
    if (!response.ok) {
      console.error("Failed to fetch products, status:", response.status);
      return;
    }

    const products = await response.json();

    if (!Array.isArray(products) || products.length === 0) {
      console.error("No products available for export!");
      return;
    }

    const headers = ["Code", "Name", "Amount", "Expiry Date", "User", "Provider"];
    let data = [headers];

    products.forEach((product) => {
      data.push([
        product.code || "N/A",
        product.name || "N/A",
        product.amount || 0,
        product.expiry_date || "N/A",
        product.user_id?.user || "Unknown",
        product.provider_id?.name || "Unknown",
      ]);
    });

    const ws = XLSX.utils.aoa_to_sheet(data);

    // Apply styles to headers
    const range = XLSX.utils.decode_range(ws["!ref"]);
    for (let col = range.s.c; col <= range.e.c; col++) {
      const cell_address = XLSX.utils.encode_cell({ r: 0, c: col });
      if (!ws[cell_address]) continue;

      ws[cell_address].s = {
        font: { bold: true, color: { rgb: "FFFFFF" } }, // White text
        fill: { fgColor: { rgb: "4F81BD" } }, // Blue background
        alignment: { horizontal: "center", vertical: "center" },
      };
    }

    // Resize columns
    ws["!cols"] = [
      { wch: 15 }, // Code
      { wch: 30 }, // Name
      { wch: 10 }, // Amount
      { wch: 15 }, // Expiry Date
      { wch: 20 }, // User
      { wch: 25 }, // Provider
    ];

    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "All Products");

    XLSX.writeFile(wb, "products.xlsx");
  } catch (error) {
    console.error("Error exporting to Excel:", error);
  }
}


