import "./index.css";
import React, {useState} from "react";
import {navigationTabs} from "../../tabs";
import Nav from "react-bootstrap/Nav";
import {UserInfo} from "./UserInfo";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router";

export default function LeftPanel({tabId}) {
    const [activeOption] = useState(navigationTabs.find(option => option.tab === tabId))
    const navigate = useNavigate()
    return <div className="LeftPanel">
        <Nav id="Navigation" variant='pills' activeKey={activeOption?.tab} defaultActiveKey="/home"
             className="flex-column">
            {navigationTabs.map((option, index) => (
                <Nav.Link key={option.tab} href={option.tab}>{option.name}</Nav.Link>
            ))}
        </Nav>
        <hr className="menu-divider"/>
        <Button className="new-assistant-button"
                onClick={() => navigate("/assistants/new")}>
            Create Assistant
        </Button>
        <div className="left-panel-filler"></div>
        <UserInfo/>
    </div>
}
