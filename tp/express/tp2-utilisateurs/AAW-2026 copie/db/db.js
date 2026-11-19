// librairie permettant de générer un uuid (identifiant totalement aléatoire)
const { randomUUID } = require('crypto');

const users = [
    {
        id: randomUUID(),
        name: "tdtxdqtsx",
        url:"https://i.pravatar.cc/160?u="+randomUUID()
    },
    {
        id: randomUUID(),
        name: "tdtxdsx",
        url:"https://i.pravatar.cc/160?u="+randomUUID()
    }
]

module.exports = users;

