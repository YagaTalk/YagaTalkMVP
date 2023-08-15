import React from 'react';
import './SettingsWindow.css';
const SettingsWindow = () => {
    return (
        <div className="settings-window">
            <div className="user-name">Admin Name</div>
            <div className="buttons-container">
                <button>Log Out</button>
                <button>Change Password</button>
            </div>
        </div>
    );
};

export default SettingsWindow;