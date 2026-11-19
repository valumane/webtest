const express = require("express");
const router = express.Router();
const users = require("../db/db");

//API permettant de récupérer la liste des utilisateurs
router.get("/users", (req, res) => {
    res.json(users);
})

//Route permettant d'ajouter un like à un utilisateur
// l'écriture :id induit l'existance d'un pathParam
// ainsi lorsque le client nous appellera avec une url du type
// /api/users/like/add/XXXXXXX
// alors XXXXXX sera considéré comme le parametre id et sera donc récupérable
// via req.params.id
router.post("/users/like/add/:id", (req, res) => {
    const id = req.params.id;
    //recherche de l'utilisateur dans notre liste
    let userToUpdate = users.find((user) => user.id === id);
    if (userToUpdate) {
        // MAJ du nombre de like, si celui ci existe alors on l'incremente,
        // sinon on le passe à 1
        userToUpdate.like = userToUpdate.like ? userToUpdate.like + 1 : 1;

        // on renvoie la liste des utilisateurs
        // le statut sera à 200 par défaut
        res.json(users);
    }else{
        res.status(404).send("L'utilisateur demandé n'existe pas")
    }
})


module.exports = router;

