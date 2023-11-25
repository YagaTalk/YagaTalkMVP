import './index.css';
import {ListGroup} from "react-bootstrap";
import {assistantsList} from "../../mock-data";
import React from "react";

export function AssistantsList() {
    return <ListGroup className="AssistantsList">
        {assistantsList.map(assistant => (
            <AssistantsListItem assistant={assistant}/>
        ))}
    </ListGroup>
}

function AssistantsListItem({assistant}) {
    return (
        <ListGroup.Item key={assistant.id} className="AssistantsListItem">
            <span>
                <span className="assistant-name">{assistant.name}</span>
                <AssistantStatusBadge status={assistant.status}/>
            </span>
            <p>{assistant.description}</p>
            <p>{assistant.created}</p>
        </ListGroup.Item>)
}

function AssistantStatusBadge({status}) {
    const badgesValues = {
        "ACTIVE": {
            text: "active",
            className: "active"
        },
        "DRAFT": {
            text: "draft",
            className: "draft"
        },
    }
    const badge = badgesValues[status]
    return <span className={"assistant-status-badge assistant-status-badge-" + badge.className}>{badge.text}</span>
}