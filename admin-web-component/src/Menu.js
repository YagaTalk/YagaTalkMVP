import React, { useState } from 'react';
import './Menu.css';
import { Link } from 'react-router-dom';
import SettingsWindow from './SettingsWindow.js';


function Menu() {
    const [showSettings, setShowSettings] = useState(false);

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
                    <Link to="/context/" className="btn btn-primary btn-lg m-2">
                        Show All Context
                    </Link>
                    <Link to="/context/add" className="btn btn-primary btn-lg m-2">
                        Add New Context
                    </Link>
                </div>
            </div>
            {showSettings && <SettingsWindow onClose={() => setShowSettings(false)}/>}
        </div>
    );
}

export default Menu;