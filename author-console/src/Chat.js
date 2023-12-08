import React, {useEffect, useRef, useState} from 'react';
import './Chat.css';
import {BACKEND_URL, BASE_PATH} from './Config';

function Chat({sessionId}) {
    const [error, setError] = useState(null);
    const chatBodyRef = useRef(null);
    const txtInputRef = useRef(null);
    const [messages, setMessages] = useState([]);
    const [shouldScrollToBottom, setShouldScrollToBottom] = useState(true);


    useEffect(() => {
        if (!sessionId) return;

        getHistory(sessionId);
        const intervalId = setInterval(() => getHistory(sessionId), 1000);
        return () => {
            clearInterval(intervalId);
        };
    }, [sessionId]);

    useEffect(() => {
        if (chatBodyRef.current) {
            const {scrollHeight, clientHeight, scrollTop} = chatBodyRef.current;
            setShouldScrollToBottom(scrollHeight - clientHeight === scrollTop);
        }
    }, [messages]);

    const getHistory = async (chatSessionId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/sessions/${chatSessionId}/messages`);
            if (response.ok) {
                const data = await response.json();
                const formattedMessages = data.map(({role, content}) => ({role, text: content}));
                setMessages(formattedMessages);
            } else {
                console.error('Failed to fetch messages from the backend.');
            }
        } catch (error) {
            console.error('Error occurred while fetching messages:', error);
        }
    };

    const renderUserMessage = () => {
        const userInput = txtInputRef.current.value;
        addMessage(userInput, 'user');
        txtInputRef.current.value = '';
        sendUserMessageToServer(userInput, sessionId);
    };

    const sendUserMessageToServer = async (userInput, chatSessionId) => {
        try {
            const response = await fetch(`${BACKEND_URL}/api/sessions/${chatSessionId}/messages`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({text: userInput}),
            });
            if (!response.ok) {
                console.error('Failed to send the user message to the server.');
            }
        } catch (error) {
            console.error('Error occurred while sending the user message:', error);
        }
    };

    const addMessage = (txt, type) => {
        setMessages((prevMessages) => [...prevMessages, {role: type, text: txt}]);
    };

    useEffect(() => {
        // Set the scroll position to the bottom if shouldScrollToBottom is true
        if (shouldScrollToBottom && chatBodyRef.current) {
            chatBodyRef.current.scrollTop = chatBodyRef.current.scrollHeight;
        }
    }, [shouldScrollToBottom]);

    return (
        <>
            {error && <h1>{error}</h1>}
            {!error && sessionId && (
                <div className="container">
                    <div className="chat-header">
                        <div className="logo">
                            <img src={`${BASE_PATH}/images/icon.jpg`} alt="icon"/>
                        </div>
                        <div className="title">YagaTalk</div>
                    </div>
                    <div ref={chatBodyRef} className="chat-body">
                        {messages.map((message, index) => (
                            <div key={index} className={message.role === 'user' ? 'user-message' : 'assistant-message'}>
                                {message.text}
                            </div>
                        ))}
                    </div>
                    <div className="chat-input">
                        <div className="input-sec">
                            <input ref={txtInputRef} type="text" id="txtInput" placeholder="Let's talk!" autoFocus/>
                        </div>
                        <button onClick={renderUserMessage} className="send">
                            <img src={`${BASE_PATH}/images/send.svg`} alt="send"/>
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default Chat;