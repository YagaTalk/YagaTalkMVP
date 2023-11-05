import {useNavigate, useParams} from "react-router-dom";
import React, {useContext, useEffect, useState} from "react";
import {Button, Modal, Table} from "react-bootstrap";
import { format } from "date-fns";
import './AssistantDetail.css';
import {BACKEND_URL} from "./Config";
import {AuthContext} from "react-oauth2-code-pkce";
import axios from "axios";

function AssistantDetail() {
    const { assistantId } = useParams();
    const [assistantData, setAssistantData] = useState(null);
    const [showModal, setShowModal] = useState(false);
    const navigate  = useNavigate();


    const { token } = useContext(AuthContext);

    useEffect(() => {
        const fetchData = async () => {
            const url = `${BACKEND_URL}/api/assistants/${assistantId}`;
            const headers = { 'Authorization': `Bearer ${token}` };

            try {
                const response = await axios.get(url, { headers });
                setAssistantData(response.data);
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

        fetchData();
    }, [token, assistantId]);

    const goToChatHistory = () => {
        navigate('/assistants/'+assistantId+'/chatHistory');
    };

    const goToEmbeddingInstruction = ()=> {
        navigate('/assistants/'+assistantId+'/instruction')
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