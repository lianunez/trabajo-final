function logout(){
    sessionStorage.removeItem("userInfo")
    setInterval(()=>{
        location.href = "/"
    }, 2000)
}

logout();
