import React, {useContext} from 'react';
import './SettingsWindow.css';
import {Button} from "react-bootstrap";
import {AuthContext} from "./auth";

const SettingsWindow = ({userName}) => {
    const authContext = useContext(AuthContext);
    const handleLogout = async () => {
        await authContext.logout();
    };

    return (
        <div className="settings-window">
            <div className="user-name">
                <h3 className="user-name">{userName}</h3>
            </div>
            <div className="buttons-container">
                <Button variant="primary">Change Password</Button>
                <Button variant="primary" onClick={handleLogout}>
                    Log Out
                </Button>
            </div>
        </div>
    );
};

export default SettingsWindow;

