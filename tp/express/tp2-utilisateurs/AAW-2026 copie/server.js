const express = require("express");

const app = express();
app.use(express.static("./public"));
app.use(express.json());

const userRouter = require("./route/userRouter");

// utilisation du userRouter sur le path /api
// il y aura donc une concaténation entre les routes définies dans
// userRouter et "/api"
// ex : "/api/users"
app.use("/api", userRouter);

//Démarrage du serveur sur le port 3000
app.listen(3000, () => {
    console.log("serveur démarré");
})
