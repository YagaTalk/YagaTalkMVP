import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";

function ContextDetail() {
    const { contextId } = useParams();
    const [contextData, setContextData] = useState(null);

    useEffect(() => {
        const fetchData = () => {
            fetch(`http://localhost:8082/api/chat/sessions/context/${contextId}`)
                .then(response => response.json())
                .then(data => setContextData(data))
                .catch(error => console.error('Error fetching contexts:', error));
        };

        fetchData();
    }, [contextId]);

    return (
        <div className="context-table-page">
            <div className="chat-header">
                YagaTalk
            </div>
            <div className="action-buttons">
                <button className="action-button">Test Chat</button>
                <button className="action-button">Archive Context</button>
            </div>
            {contextData && (
                <div className="context-table">
                    <table>
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
                            <td>{contextData.createdTime}</td>
                            <td>{contextData.content}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            )}
        </div>
    );
}

export default ContextDetail;