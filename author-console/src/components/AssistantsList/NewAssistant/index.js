import './index.css';
import React, {useContext, useState} from "react";
import {Button, Form, Modal} from "react-bootstrap";
import InputGroupText from "react-bootstrap/InputGroupText";
import {useNavigate} from "react-router";
import {AuthContext} from "../../../auth";
import {BACKEND_URL} from "../../../Config";
import axios from "axios";
import Chat from "../../TestChat";

export function NewAssistant() {
    const [creationInProgress, setCreationInProgress] = useState(true)
    const [isTestPressed, setIsTestPressed] = useState(false)
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

    const handleTest = async (event, isTestPressed, assistantId) => {
        event.preventDefault();

        const url2 = `${BACKEND_URL}/api/assistants?isTestSession=true`;

        const url = `${BACKEND_URL}/api/assistants/edit/${assistantId}`;
        const headers = {'Authorization': `Bearer ${token}`};
        const data = {
            name,
            content,
            description
        };

        if (isTestPressed) {
            try {
                console.log("----------------------------------")
                console.log(url)
                const response = await axios.post(url, data, {headers});
                console.log("test button was pressed")
                console.log("/////////////handleTest/////////////")
                console.log('Response:', response.data.id);
                console.log("////////////handleTest/////////////////")
                // resetState();
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
        } else {
            const response = await axios.post(url2, data, {headers});
            console.log("///////////////getAssistantId////////////////////")
            console.log('Response:', response.data.id);
            console.log("///////////////getAssistantId////////////////////")
            // resetState();
            setCreatedAssistantId(response.data.id);
            return response.data.id;
        }
    };
    const handleSubmit = async (event, isTestSession, isTestPressed) => {
        event.preventDefault();

        const url = `${BACKEND_URL}/api/assistants?isTestSession=${isTestSession}`;

        const url2 = `${BACKEND_URL}/api/assistants/${createdAssistantId}`;
        const headers = {'Authorization': `Bearer ${token}`};
        const data = {
            name,
            content,
            description
        };

        if (isTestPressed) {
            try {
                console.log("----------------------------------")
                console.log(url2)
                const response = await axios.post(url2, data, {headers});
                console.log("test button was pressed")
                console.log("/////////////handleSubmit//////////////////////")
                console.log('Response:', response.data.id);
                console.log("////////////handleSubmit/////////////////////////")
                // resetState();
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
        } else {
            try {
                const response = await axios.post(url, data, {headers});
                console.log("//////////handleSubmit////////////////////")
                console.log('Response:', response.data.id);
                console.log("//////handleSubmit////////////////////")
                // resetState();
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
        }
    };


    const testChatEnabled = !!createdAssistantId;

    const navigate = useNavigate()
    const closeModal = () => {
        // resetState()
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
                    {testChatEnabled && <Chat assistantId={createdAssistantId}/>}
                </Modal.Body>
                <Modal.Footer>
                    <Button
                        onClick={(event) => {
                            event.preventDefault();
                            handleTest(event, isTestPressed, createdAssistantId)
                            setIsTestPressed(true)
                            console.log("created assistant after test was pressed:" + createdAssistantId)
                        }}>Test</Button>
                    <Button
                        onClick={(event) => {
                            event.preventDefault();
                            console.log("Is test button pressed " + isTestPressed)
                            handleSubmit(event, false, isTestPressed);
                            console.log("created assistant after submit was pressed:" + createdAssistantId)
                            closeModal()
                        }}
                        variant="primary">Create</Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}
