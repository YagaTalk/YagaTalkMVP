import "./index.css";
import {Button, FigureImage} from "react-bootstrap";
import {AuthContext} from "../../../auth";
import {useContext} from "react";

export function UserInfo() {
    const {userInfo} = useContext(AuthContext);
    const preferredUsername = JSON.parse(JSON.stringify(userInfo)).preferred_username;

    return (<div className="UserInfo">
        <span className="username">
            <UserIcon/>
            <span className="username-text">{preferredUsername}</span>
        </span>
        <Button className="logout-button">Logout</Button>
    </div>)
}

function UserIcon() {
    return <svg
        className="username-icon"
        xmlns="http://www.w3.org/2000/svg" width="100" height="100" fill="currentColor"
        viewBox="0 0 16 16">
        <path
            d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6m2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0m4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4m-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664z"/>
    </svg>
}