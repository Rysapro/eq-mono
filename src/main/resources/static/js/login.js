document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    const errorMessage = document.getElementById('errorMessage');

    // Проверка наличия токена и автоматическое перенаправление на dashboard.html
    const token = localStorage.getItem('authToken');
    const tokenExpires = localStorage.getItem('tokenExpires');
    
    if (token && tokenExpires && parseInt(tokenExpires) > Date.now()) {
        // Токен существует и не просрочен - перенаправляем на админ-панель
        console.log('Найден существующий токен, перенаправление на админ-панель');
        window.location.href = '/admin.html';
        return;
    }
    
    // Обработчик отправки формы логина
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const fullName = document.getElementById('fullName').value.trim();
        const password = document.getElementById('password').value;
        
        if (!fullName || !password) {
            showError('Пожалуйста, заполните все поля');
            return;
        }
        
        // Разделение полного имени на составляющие
        const nameParts = fullName.split(' ');
        if (nameParts.length !== 3) {
            showError('ФИО должно содержать фамилию, имя и отчество, разделенные пробелами');
            return;
        }
        
        const surname = nameParts[0];
        const name = nameParts[1];
        const patronymic = nameParts[2];
        
        // Очистка сообщения об ошибке
        errorMessage.textContent = '';
        
        // Создание данных для отправки на сервер
        const loginData = {
            surname: surname,
            name: name,
            patronymic: patronymic,
            password: password
        };
        
        console.log('Отправляемые данные:', loginData);
        
        // Отправка запроса на сервер
        fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(loginData)
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Неверные учетные данные');
            }
            return response.json();
        })
        .then(data => {
            // Сохранение токена и перенаправление на главную страницу
            localStorage.setItem('authToken', data.token);
            localStorage.setItem('tokenExpires', Date.now() + data.expiresIn);
            
            // Перенаправление на админ-панель после успешного входа
            window.location.href = '/admin.html';
        })
        .catch(error => {
            showError('Ошибка авторизации: неверное ФИО или пароль');
            console.error('Ошибка авторизации:', error);
        });
    });
    
    // Функция для отображения сообщения об ошибке
    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = 'block';
    }
}); 