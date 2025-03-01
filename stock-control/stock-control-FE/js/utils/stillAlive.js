
setInterval(async ()=>{
  try{
    console.log('session check started...');
    const userInfo = JSON.parse(sessionStorage.getItem("userInfo"));
    const response = await fetch("http://127.0.0.1:8080/session/alive", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${userInfo.jwt}`,
      },
    });
    
    if (response.status != 200) {
      sessionStorage.removeItem("userInfo");
      location.href = "/";
    }
  }
  catch(error){
    console.error(error);
  }
  }, 180000);