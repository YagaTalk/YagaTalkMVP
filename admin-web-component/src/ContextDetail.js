import {useNavigate, useParams} from "react-router-dom";
import React, { useEffect, useState } from "react";
import {Button, Modal, Table} from "react-bootstrap";
import { format } from "date-fns";
import './ContextDetail.css';
import {BACKEND_URL} from "./Config";

function ContextDetail() {
    const { contextId } = useParams();
    const [contextData, setContextData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const navigate  = useNavigate();

    useEffect(() => {
        const fetchData = () => {
            fetch(`${BACKEND_URL}/api/chat/context/${contextId}`)
                .then(response => response.json())
                .then(data => setContextData(data))
                .catch(error => console.error('Error fetching contexts:', error));
        };

        fetchData();
    }, [contextId]);

    const goToChatHistory = () => {
        navigate('/context/'+contextId+'/chatHistory');
    };

    const goToEmbeddingInstruction = ()=> {
        navigate('/context/'+contextId+'/instruction')
    }

    return (
        <div className="context-page">
            <div className="chat-header">
                YagaTalk
            </div>
            <div className="action-buttons">

                <Button className="action-button" onClick={() => setShowModal(true)}>Test Chat</Button>
                <Button className="action-button">Archive Context</Button>
                <Button className="action-button" onClick={goToEmbeddingInstruction}>Embedding Instruction</Button>
                <Button className="action-button" onClick={goToChatHistory}>Chat History</Button>
            </div>
            {contextData && (
                <div className="context-table">
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
                            <td>{contextData.name}</td>
                            <td>{format(new Date(contextData.createdTime), "yyyy-MM-dd HH:mm")}</td>
                            <td>{contextData.content}</td>
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

export default ContextDetail;