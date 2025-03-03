// setTimeout(userPermissionsCheck(), 4000);
// function userPermissionsCheck() {
//     const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
//     if (!userInfo || !userInfo.role || !Array.isArray(userInfo.role.permissions)) {
//       console.error("Invalid userInfo structure:", userInfo);
//       return;
//     }
  
//     const actions = [
//       document.getElementById("products.create"),
//       document.getElementById("providers.create"),
//       document.getElementById("products.check"),
//       document.getElementById("products.updates"),
//     ];
  
//     const permissions = userInfo.role.permissions;
//     console.log(permissions);
  
//     actions.forEach((action) => {
//       if (action) { 
//         const actionId = action.id;
//         console.log(actionId);
//         if (!permissions.includes(actionId)) { 
//           action.style.display = 'none';
//         }
//       }
//     });
//   }
  
