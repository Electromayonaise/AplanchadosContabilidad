document.getElementById('upload-form').addEventListener('submit', async function(event) {
    event.preventDefault();

    const fileInput = document.getElementById('file');
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);

    const response = await fetch('/api/upload', {
        method: 'POST',
        body: formData
    });

    const result = await response.json();
    document.getElementById('upload-result').innerText = JSON.stringify(result, null, 2);
});
