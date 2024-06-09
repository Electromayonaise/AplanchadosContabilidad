document.getElementById('summary-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const summaryData = {
        initialBalance: document.getElementById('initialBalance').value,
        finalBalance: document.getElementById('finalBalance').value,
        totalIncome: document.getElementById('totalIncome').value,
        totalExpenses: document.getElementById('totalExpenses').value,
        managementAmount: document.getElementById('managementAmount').value,
        netBalance: document.getElementById('netBalance').value,
        date: document.getElementById('date').value
    };

    const response = await fetch('/api/summaries', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(summaryData)
    });

    if (response.ok) {
        loadSummaries();
    } else {
        console.error('Failed to create summary');
    }
});

async function loadSummaries() {
    const response = await fetch('/api/summaries');
    const summaries = await response.json();

    const tbody = document.getElementById('summaries-table-body');
    tbody.innerHTML = '';

    summaries.forEach(summary => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${summary.date}</td>
            <td>${summary.initialBalance}</td>
            <td>${summary.finalBalance}</td>
            <td>${summary.totalIncome}</td>
            <td>${summary.totalExpenses}</td>
            <td>${summary.managementAmount}</td>
            <td>${summary.netBalance}</td>
        `;
        tbody.appendChild(row);
    });
}

loadSummaries();
