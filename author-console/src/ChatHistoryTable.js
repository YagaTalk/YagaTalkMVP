import React, {useContext, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {format} from "date-fns";
import {Modal} from "react-bootstrap";
import {BACKEND_URL} from "./Config";
import axios from "axios";
import {AuthContext} from "./auth";


function ChatHistoryTable() {
    const { assistantId} = useParams();
    const [chatSessions,setChatSessions] = useState([]);
    const [showModal, setShowModal] = useState(false);

    const {token} = useContext(AuthContext);

    useEffect(() => {
        const fetchData = async () => {
            const url =`${BACKEND_URL}/api/sessions?assistantId=${assistantId}`
            const headers = { 'Authorization': `Bearer ${token}` };


            try {
                const response = await axios.get(url, { headers });
                setChatSessions(response.data);
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
    }, [token,assistantId]);

    return (
        <div className="ChatHistory-page">
            <div className="chat-header">
                YagaTalk
            </div>

            <div className="assistant-list">
                {chatSessions.map(chatSession => (
                    <div className="assistant-item-container" key={chatSession.id}>
                        <li
                            onClick={() => setShowModal(true)}
                            className="assistant-item"
                        >
                            <div className="assistant-info">
                                <div className="assistant-date">{format(new Date(chatSession.createdTime), "yyyy-MM-dd HH:mm")}</div>
                            </div>
                        </li>
                    </div>
                ))}
            </div>

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

export default ChatHistoryTable;