const API = '/api/v1/todos';
const rows = document.querySelector('#rows');
const form = document.querySelector('#new-todo');

async function load() {
    const res = await fetch(API);
    const data = await res.json();

    rows.innerHTML = '';

    data.forEach(todo => {
        const tr = document.createElement('tr');

        for (const value of [
            todo.title,
            todo.priority,
            new Date(todo.createdAt).toLocaleString()
        ]) {
            const td = document.createElement('td');
            td.textContent = value;
            tr.append(td);
        }

        const td = document.createElement('td');
        const button = document.createElement('button');
        button.textContent = 'Supprimer';
        button.className = 'del';
        button.dataset.id = todo.id;
        td.append(button);
        tr.append(td);

        rows.append(tr);
    });
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const fd = new FormData(form);

    const res = await fetch(API, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            title: fd.get('title'),
            priority: fd.get('priority')
        })
    });

    if (!res.ok) {
        alert('Erreur');
        return;
    }

    form.reset();
    await load();
});

rows.addEventListener('click', async (e) => {
    if (e.target.classList.contains('del')) {
        const id = e.target.dataset.id;

        const res = await fetch(`${API}/${id}`, {
            method: 'DELETE'
        });

        if (res.status === 204) await load();
    }
});

load();