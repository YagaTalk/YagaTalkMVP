import './index.css';
import {ListGroup} from "react-bootstrap";
import {assistantsList} from "../../mock-data";
import React from "react";
import {AssistantView} from "./AssistantView";
import {useNavigate} from "react-router";
import {NewAssistant} from "./NewAssistant";

export function AssistantsList({selectedAssistantId, createNew}) {
    const selectedAssistant = !createNew && !!selectedAssistantId && assistantsList.find(assistant => assistant.id === selectedAssistantId)
    // const shortAssistantsList = !!selectedAssistantId
    const shortAssistantsList = false
    return <div className="AssistantsList">
        <ListGroup className="AssistantsListItems">
            {assistantsList.map(assistant => (
                <AssistantsListItem
                    key={assistant.id}
                    assistant={assistant}
                    short={shortAssistantsList}
                    selected={assistant.id === selectedAssistant?.id}
                />
            ))}
        </ListGroup>
        {createNew && <NewAssistant/>}
        {!!selectedAssistant && <AssistantView assistant={selectedAssistant}/>}
    </div>
}

function AssistantsListItem({assistant, short, selected}) {
    const navigate = useNavigate()
    const className = "AssistantsListItem" + (selected ? " selected" : "")
    return (
        <ListGroup.Item
            onClick={() => navigate("/assistants/" + assistant.id)}
            key={assistant.id}
            className={className}>
            <span>
                <span className="assistant-name">{assistant.name}</span>
                <AssistantStatusBadge status={assistant.status}/>
            </span>
            {!short && <>
                <p>{assistant.description}</p>
                <p>{assistant.created}</p>
            </>}
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