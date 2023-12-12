import './index.css';
import React, {useContext, useState} from "react";
import {Button, Form, Modal} from "react-bootstrap";
import InputGroupText from "react-bootstrap/InputGroupText";
import {useNavigate} from "react-router";
import {AuthContext} from "../../../auth";
import {BACKEND_URL} from "../../../Config";
import axios from "axios";

export function NewAssistant() {
    const [creationInProgress, setCreationInProgress] = useState(true)
    const [name, setName] = useState('')
    const [description, setDescription] = useState('')
    const [content, setContent] = useState('')
    const [createdAssistantId, setCreatedAssistantId] = useState(null)
    const {token} = useContext(AuthContext);

    const resetState = () => {
        setName('')
        setDescription('')
        setContent('')
        setCreatedAssistantId(null)
    }

    const handleSubmit = async (event) => {
        event.preventDefault();

        const url = `${BACKEND_URL}/api/assistants`;
        const headers = {'Authorization': `Bearer ${token}`};
        const data = {
            name,
            content,
        };

        try {
            const response = await axios.post(url, data, {headers});
            console.log('Response:', response.data);
            resetState();
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.log(`Error calling endpoint ${url}: ${error}`);
                if (error.response) {
                    console.error(`Error: ${error.response.status}`);
                } else {
                    console.error(error);
                }
            } else {
                throw error;
            }
        }
    };


    const testChatEnabled = !!createdAssistantId;

    const navigate = useNavigate()
    const closeModal = () => {
        resetState()
        setCreationInProgress(false)
        !!createdAssistantId ? navigate("/assistants/" + createdAssistantId) : navigate("/assistants")
    }

    const className = testChatEnabled ? "NewAssistant with-test-chat" : "NewAssistant"
    const dialogClassName = testChatEnabled ? "modal-dialog with-test-chat" : "modal-dialog"
    return (
        <>
            <Modal
                className={className}
                dialogClassName={dialogClassName}
                show={creationInProgress} onHide={closeModal}>
                <Modal.Header closeButton>
                    <Modal.Title>Create Assistant</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <div className="assistant-form-controls">
                        <Form>
                            <InputGroupText>
                                <Form.Label>Title</Form.Label>
                                <Form.Control onChange={e => setName(e.target.value)} type="text"
                                              placeholder="Enter title"/>
                            </InputGroupText>
                            <InputGroupText>
                                <Form.Label>Description</Form.Label>
                                <Form.Control onChange={e => setDescription(e.target.value)}
                                              type="text" placeholder="Enter description"/>
                            </InputGroupText>
                            <InputGroupText>
                                <Form.Label>Instruction</Form.Label>
                                <Form.Control
                                    onChange={e => setContent(e.target.value)}
                                    as="textarea"
                                    rows={15}
                                    type="text"
                                    placeholder="Enter instruction"
                                />
                            </InputGroupText>
                        </Form>
                    </div>
                    {testChatEnabled && <TestChat assistantId={createdAssistantId}/>}
                </Modal.Body>
                <Modal.Footer>
                    <Button
                        onClick={() => {
                            handleSubmit();
                        }}>Test</Button>
                    <Button
                        onClick={() => {
                            closeModal()
                        }}
                        variant="primary">Create</Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

function TestChat({assistantId}) {
    return <iframe className="test-chat" src={"http://localhost:3001/" + assistantId}/>
}

