//on récupère l'élément main
const main = document.querySelector('main');

// chargement des utilisateurs
async function load() {
    // Appel du serveur
    fetch("/api/users")
        .then((res) => {
            if (res.status === 200)
                return res.json()
            console.log(res);
        })
        .then((users) => {
            // reinitialisation de notre zone d'affichage
            main.innerHTML = "";
            // Pour chaque utilisateur on construit un element article
            // et on l'ajoute dans notre balise main
            users.forEach((user) => {
                creatChild(user);
            })
        })


}

load();

async function incremenrLike(id) {
    return await fetch("/api/users/like/add/" + id, {method: "POST"})
        .then((res) => {
            if (res.status !== 200)
                alert("Problème lors de la mise à jour d'un utilisateur");
        });
}

function creatChild(user) {
    // on créé un élément article
    let htmlElement = document.createElement("article");
    // on lui ajoute la class "card"
    htmlElement.classList = "card";

    let img = document.createElement("img");
    img.classList = "avatar";
    img.setAttribute("src", user.url);
    img.setAttribute("alt", "Avatar de l’utilisateur");
    htmlElement.append(img);

    // on créé un élément h2
    let h2Name = document.createElement("h2");
    // on y insére le nom de l'utilisateur
    h2Name.textContent = user.name;
    htmlElement.append(h2Name);

    // on créé un élément h2
    let pLike = document.createElement("p");
    pLike.classList = "likes";

    let spanCounter = document.createElement("span");
    spanCounter.classList = "count";
    spanCounter.textContent = user.like || "0";

    pLike.append("❤️ ");
    pLike.append(spanCounter);
    htmlElement.append(pLike);

    // on créé un bouton like
    let likeButton = document.createElement("button");
    likeButton.classList = "like-btn";
    likeButton.setAttribute("type", "button");
    likeButton.setAttribute("aria-pressed", "false");
    likeButton.textContent = "LIKE"

    // on lui ajoute un handler permettant de gérer le click utilisateur
    likeButton.addEventListener('click', async () => {
        likeButton.setAttribute("aria-pressed", "true");
        await incremenrLike(user.id);
        await load();
        likeButton.setAttribute("aria-pressed", "false");
    });

    htmlElement.append(likeButton);
    main.append(htmlElement)

}




















