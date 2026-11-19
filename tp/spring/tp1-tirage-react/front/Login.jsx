import {useEffect, useState} from "react";

export default (props) => {

    const [login, setLogin] = useState("");
    const [pwd, setPwd] = useState("");

    useEffect(() => {
        let user = sessionStorage.getItem("user");
        user = user && JSON.parse(user);
        if(user && user.login){
            window.location = "/tirages.html";
        }
    }, []);

    const handleLogin = (e) => {
        e.preventDefault();

        const params = new URLSearchParams({login, password: pwd});
        fetch("/login", {
            method: "POST",
            body: params
        })
            .then(res => {
                if (res.status !== 200) {
                    throw new Error("Problème de connexion")
                }
                return res.json();
            })
            .then(user => {
                console.log("user", user)
                sessionStorage.setItem("user", JSON.stringify(user))
                window.location = "/tirages.html";
            })
    }

    return (
        <div className="login">
            <h1>Connexion</h1>

            <form onSubmit={handleLogin}>
                <div>
                    <label htmlFor="login">login</label>
                    <input value={login} type="text" id="login" name="login"
                           onChange={(e) => setLogin(e.currentTarget.value)}/>
                </div>
                <div>
                    <label htmlFor="pwd">password</label>
                    <input type="text" id="pwd" name="password"
                           onChange={(e) => setPwd(e.currentTarget.value)}/>
                </div>
                <button type="submit">Se connecter</button>
            </form>
        </div>
    )
}
