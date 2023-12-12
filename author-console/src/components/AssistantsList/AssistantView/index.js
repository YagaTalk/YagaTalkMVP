import './index.css';
import React from "react";
import {Button} from "react-bootstrap";
import {useNavigate} from "react-router";
import {format} from "date-fns";

export function AssistantView({assistant}) {
    return (
        <div className={"AssistantView"}>
            <AssistantViewHeader assistant={assistant}/>
            <div className="assistant-attributes">

                <div className="assistant-attribute">
                    <span className="assistant-attribute-key">Description </span>
                    <span className="assistant-attribute-value">{assistant.description}</span>
                </div>
                <div className="assistant-attribute">
                    <span className="assistant-attribute-key">Created </span>
                    <span
                        className="assistant-attribute-value">{format(new Date(assistant.createdTime), "yyyy-MM-dd HH:mm")}</span>
                </div>
                <div className="assistant-attribute">
                    <span className="assistant-attribute-key">Updated </span>
                    <span
                        className="assistant-attribute-value">{format(new Date(assistant.updatedTime), "yyyy-MM-dd HH:mm")}</span>
                </div>
                <div className="assistant-attribute">
                    <span className="assistant-attribute-key">Status </span>
                    <span className="assistant-attribute-value">{assistant.status}</span>
                </div>

                <div className="assistant-instruction">
                    <h2>Instruction</h2>
                    {assistant.content}
                </div>
            </div>

        </div>
    )
}

function AssistantViewHeader({assistant}) {
    const navigate = useNavigate()
    return <div className={"AssistantViewHeader"}>

        <span>
            <Button
                className={"assistant-close-button"}
                onClick={() => navigate("/assistants")}>
                <svg
                    xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor"
                    viewBox="0 0 16 16">
                    <path
                        d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                </svg>
            </Button>
            <span className="assistant-name">{assistant.name}</span>
        </span>

    </div>
}