import {useNavigate, useParams} from "react-router-dom";
import React, { useEffect, useState } from "react";
import {Button, Modal, Table} from "react-bootstrap";
import { format } from "date-fns";
import './AssistantDetail.css';
import {BACKEND_URL} from "./Config";

function AssistantDetail() {
    const { assistantId } = useParams();
    const [assistantData, setAssistantData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const navigate  = useNavigate();

    useEffect(() => {
        const fetchData = () => {
            fetch(`${BACKEND_URL}/api/chat/assistant/${assistantId}`)
                .then(response => response.json())
                .then(data => setAssistantData(data))
                .catch(error => console.error('Error fetching assistants:', error));
        };

        fetchData();
    }, [assistantId]);

    const goToChatHistory = () => {
        navigate('/assistant/'+assistantId+'/chatHistory');
    };

    const goToEmbeddingInstruction = ()=> {
        navigate('/assistant/'+assistantId+'/instruction')
    }

    return (
        <div className="assistant-page">
            <div className="chat-header">
                YagaTalk
            </div>
            <div className="action-buttons">

                <Button className="action-button" onClick={() => setShowModal(true)}>Test Chat</Button>
                <Button className="action-button">Archive Assistant</Button>
                <Button className="action-button" onClick={goToEmbeddingInstruction}>Embedding Instruction</Button>
                <Button className="action-button" onClick={goToChatHistory}>Chat History</Button>
            </div>
            {assistantData && (
                <div className="assistant-table">
                    <Table striped bordered>
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Created Time</th>
                            <th>Content</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>{assistantData.name}</td>
                            <td>{format(new Date(assistantData.createdTime), "yyyy-MM-dd HH:mm")}</td>
                            <td>{assistantData.content}</td>
                        </tr>
                        </tbody>
                    </Table>
                </div>
            )}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Body>
                    <div className="modal-content">
                        This is a test chat modal.
                    </div>
                </Modal.Body>
            </Modal>
        </div>
    );
}

export default AssistantDetail;