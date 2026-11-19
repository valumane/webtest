import {useEffect, useState} from "react";
import "./app.scss";
import toast from "react-hot-toast";

export default ({message}) => {

    const [tirages, setTirages] = useState([]);

    const chargerData = () => {
        fetch("/api/tirages")
            .then(res => res.json())
            .then(mess => {
                setTirages(mess);
            })

    }

    const addTirage = () => {
        toast.promise(()=>{
            return fetch("/api/tirages", {method: "POST"})
                .then(res => {
                    if (res.status !== 200) {
                        if (res.status === 423) {
                            throw new Error("Il n'y a plus de sujet disponible !");
                        }
                    }
                    chargerData();
                })
        }, {
            loading: "Tirage en cours...",
            success: ({ groupe, sujet }) => `Groupe ${groupe} : sujet ${sujet}`,
            error: (e) => e.message,
        });

    }

    const deconnect = ()=>{
        sessionStorage.setItem("user", null);
        window.location = "/";
    }

    useEffect(() => {
        let user = sessionStorage.getItem("user");
        user = user && JSON.parse(user);
        if(!user || !user.login){
            window.location = "/";
        }else {
            chargerData();
        }
    }, []);

    const sortedTirages = tirages
        .filter(tirage => tirage.id.groupe && tirage.id.groupe.id);

    sortedTirages.sort((a, b) => {
        console.log(a, tirages[a], b, tirages[b])
        console.log(tirages[a] > tirages[b])
        return Number(a.id.groupe.id) - Number(b.id.groupe.id);
    });
    console.log(sortedTirages);

    return (
        <div className="app">
            <button onClick={deconnect}>Se deconnecter</button>
            <h1>{message}</h1>
            {
                sortedTirages && sortedTirages.length > 0 ?
                    <table>
                        <thead>
                        <tr>
                            <th>GROUPE</th>
                            <th>SUJET</th>
                        </tr>
                        </thead>
                        <tbody>

                        {
                            sortedTirages
                                .map(tirage => <tr key={tirage.id.groupe.id} className="tirage">
                                    <td>{tirage.id.groupe.id} - {tirage.id.groupe.label}</td>
                                    <td>{tirage.id.sujet.id} - {tirage.id.sujet.label}</td>
                                </tr>)
                        }
                        </tbody>
                    </table>
                    :
                    <div>Aucun tirage pour l'instant</div>
            }
            <button onClick={addTirage}>Nouveau tirage</button>
        </div>
    )
}
