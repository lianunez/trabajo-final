async function main() {
  console.info("Session check started...");
  const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));

  if (!userInfo) {
    console.log("Login, please");
    window.location.assign("./components/login-form.html");
    return;
  }

  try {
    const response = await fetch("http://127.0.0.1:8080/session/alive", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${userInfo.jwt}`,
      },
    });
    
    if (response.status === 200) {
      console.log("Login successful");

      setTimeout(() => {
        window.location.assign("./dashboard.html");
      }, 2000);

      return;
    }

    if (response.status != 200) {
      console.info("Login again, please");
      sessionStorage.removeItem("userInfo");
      window.location.assign("./components/login-form.html");
      return;
    }
  } catch (error) {
    console.error("Fetch error:", error);
  }
}

main();