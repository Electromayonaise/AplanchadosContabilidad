document.getElementById('transaction-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const transactionData = {
        document: document.getElementById('document').value,
        clientName: document.getElementById('clientName').value,
        paymentType: document.getElementById('paymentType').value,
        paymentMethod: document.getElementById('paymentMethod').value,
        amount: document.getElementById('amount').value,
        date: document.getElementById('date').value,
        detail: document.getElementById('detail').value,
        type: "entrada"  // or "salida", based on your logic
    };

    const response = await fetch('/api/transactions', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(transactionData)
    });

    if (response.ok) {
        loadTransactions();
    } else {
        console.error('Failed to create transaction');
    }
});

async function loadTransactions() {
    const response = await fetch('/api/transactions');
    const transactions = await response.json();

    const tbody = document.getElementById('transactions-table-body');
    tbody.innerHTML = '';

    transactions.forEach(transaction => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${transaction.document}</td>
            <td>${transaction.clientName}</td>
            <td>${transaction.paymentType}</td>
            <td>${transaction.paymentMethod}</td>
            <td>${transaction.amount}</td>
            <td>${transaction.date}</td>
            <td>${transaction.detail}</td>
            <td>${transaction.type}</td>
        `;
        tbody.appendChild(row);
    });
}

loadTransactions();
