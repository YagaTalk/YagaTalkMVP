import React, { useState } from 'react';
import './Menu.css';
import { Link } from 'react-router-dom';
import SettingsWindow from './SettingsWindow.js';


function Menu() {
    const [showSettings, setShowSettings] = useState(false);
    // Функция для обработки нажатия на иконку настроек
    const handleSettingsClick = () => {setShowSettings(!showSettings);};
    return (
        <div className="user-menu-page">
            <div className="chat-header">
                YagaTalk
                <div className="settings-icon" onClick={handleSettingsClick}>
                    <img src="/images/settings.png" alt="Settings" />
                </div>
            </div>
            <div className="login-page">
                <div className="button-container">
                    <Link to="/menu/context-table" className="rectangle">
                        Show All Context
                    </Link>
                    <div className="rectangle">
                        Add New Context
                    </div>
                </div>
            </div>
            {showSettings && <SettingsWindow onClose={() => setShowSettings(false)}/>}
        </div>
    );
}

export default Menu;