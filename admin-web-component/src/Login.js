import React, {useState} from 'react';
import './Login.css';
import { useNavigate } from 'react-router-dom';
function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate  = useNavigate();
    const handleLogin = () => {
        // Логика обработки входа пользователя
        navigate('/menu');
    };

    return (
        <div className="login-page">
            <div className="chat-header">
                YagaTalk
            </div>
            <div className="login-container">
                <h1>Sign in</h1>
                <form className="login-form">
                    <input
                        type="text"
                        placeholder="Login"
                        className="login-input"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        className="login-input"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                    <button className="login-button" onClick={handleLogin}>
                        Log in
                    </button>
                </form>
            </div>
        </div>
    );
}

export default Login;

