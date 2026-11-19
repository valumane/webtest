import { useEffect, useState } from "react";

const GENRES = ["roman", "science-fiction", "policier", "essai"];

export default function App() {
    // TODO : états (liste, filtre, livre sélectionné, champs du formulaire, erreurs)
    // FormData lira les champs. On garde le message d'ajout dans l'état.
    const [message, setMessage] = useState("");

    // TODO : fonction qui recharge la liste (GET /api/livres, avec ?genre= si un filtre est choisi)

    useEffect(() => {
        // TODO : charger la liste au démarrage et quand le filtre change
    }, []);

    // TODO : fonctions pour ajouter, changer l'état de lecture, supprimer, afficher le détail
    async function ajouterLivre(event) {
        // On évite le rechargement et on récupère les champs par leur nom.
        event.preventDefault();
        const formulaire = event.currentTarget;
        const livre = Object.fromEntries(new FormData(formulaire));
        // L'API attend l'année sous forme de nombre.
        livre.annee = Number(livre.annee);

        try {
            // Vite relaie cette requête JSON à Express.
            const reponse = await fetch("/api/livres", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(livre)
            });

            if (!reponse.ok) {
                // On conserve la saisie pour permettre de corriger les erreurs.
                const { erreurs } = await reponse.json().catch(() => ({}));
                setMessage(erreurs?.join(" ") || `Erreur HTTP ${reponse.status}`);
                return;
            }

            // L'ajout a réussi. Le formulaire est prêt pour un autre livre.
            setMessage("Livre ajouté !");
            formulaire.reset();
        } catch {
            setMessage("Impossible d’ajouter le livre. Vérifie le serveur.");
        }
    }

    return (
        <div style={{ padding: "2rem", maxWidth: 700 }}>
            <h1>Bibliothèque</h1>
            {/* TODO : formulaire, filtre, liste, encadré de détail */}
            <form onSubmit={ajouterLivre}>
                <label>Titre <input name="titre" required /></label>
                <label>Auteur <input name="auteur" required /></label>

                <label>
                    Genre
                    <select name="genre">
                        {GENRES.map(genre => (
                            <option key={genre} value={genre}>{genre}</option>
                        ))}
                    </select>
                </label>

                <label>Année <input name="annee" type="number" required /></label>
                <button type="submit">Ajouter</button>
            </form>

            <p role="status">{message}</p>
        </div>
    );
}
