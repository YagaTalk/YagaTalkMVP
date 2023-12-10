import './index.css';
import React, {useState} from "react";
import {Button, Form, Modal} from "react-bootstrap";
import InputGroupText from "react-bootstrap/InputGroupText";
import {useNavigate} from "react-router";

export function NewAssistant() {
    const [creationInProgress, setCreationInProgress] = useState(true)
    const [name, setName] = useState('')
    const [description, setDescription] = useState('')
    const [context, setContext] = useState('')
    const [createdAssistantId, setCreatedAssistantId] = useState(null)

    const testChatEnabled = !!createdAssistantId;

    const resetState = () => {
        setName('')
        setDescription('')
        setContext('')
        setCreatedAssistantId(null)
    }

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
                                    onChange={e => setContext(e.target.value)}
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
                            setCreatedAssistantId("a1e7e851-505b-4b62-b4de-5a56d46ee843")
                        }}>Test</Button>
                    <Button
                        onClick={() => {
                            setCreatedAssistantId("a1e7e851-505b-4b62-b4de-5a56d46ee843")
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

