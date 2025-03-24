document.addEventListener('DOMContentLoaded', function() {
    const currentUserNameElem = document.getElementById('currentUserName');
    const logoutBtn = document.getElementById('logoutBtn');
    
    // Проверка авторизации
    checkAuth();
    
    // Обработчик кнопки выхода
    logoutBtn.addEventListener('click', function() {
        logout();
    });
    
    // Функция проверки авторизации
    function checkAuth() {
        const token = localStorage.getItem('authToken');
        const tokenExpires = localStorage.getItem('tokenExpires');
        
        // Проверка наличия токена и его срока действия
        if (!token || !tokenExpires || parseInt(tokenExpires) < Date.now()) {
            logout();
            return;
        }
        
        // Загрузка данных пользователя
        fetchUserData(token);
    }
    
    // Функция загрузки данных пользователя
    function fetchUserData(token) {
        fetch('/api/user/me', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Ошибка авторизации');
            }
            return response.json();
        })
        .then(data => {
            // Отображение имени пользователя
            currentUserNameElem.textContent = data.fullName || 'Пользователь';
            
            // Здесь можно загрузить дополнительные данные для панели управления
            loadDashboardData(token);
        })
        .catch(error => {
            console.error('Ошибка загрузки данных пользователя:', error);
            logout();
        });
    }
    
    // Функция загрузки данных для панели управления
    function loadDashboardData(token) {
        // Имитация загрузки данных
        console.log('Загрузка данных панели управления...');
        
        // В реальном приложении здесь будет запрос к API
        // для получения актуальных данных очереди и статистики
    }
    
    // Функция выхода из системы
    function logout() {
        localStorage.removeItem('authToken');
        localStorage.removeItem('tokenExpires');
        window.location.href = '/index.html';
    }
}); 