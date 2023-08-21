import React from 'react';
import './SettingsWindow.css';
import {Button} from "react-bootstrap";
const SettingsWindow = () => {
    return (
        <div className="settings-window">
            <div className="user-name">
                <h3 className="user-name">Admin Name</h3>
            </div>
            <div className="buttons-container">
                <Button variant="primary">Change Password</Button>
                <Button variant="primary">Log Out</Button>
            </div>
        </div>
    );
};

export default SettingsWindow;

