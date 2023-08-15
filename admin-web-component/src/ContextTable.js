import React, { useState, useEffect } from 'react';
import './ContextTable.css';
import {useNavigate} from "react-router-dom";

function ContextTable() {
    const [contexts, setContexts] = useState([]);
    const [sortByDate, setSortByDate] = useState(true);
    const [searchTerm,setSearchTerm] = useState('');
    const [selectedYear, setSelectedYear] = useState('');
    const [selectedMonth, setSelectedMonth] = useState('');
    const [selectedDay, setSelectedDay] = useState('');// true for descending, false for ascending
    const navigate  = useNavigate();

    const handleRowClick = (contextId) => {
        navigate('/menu/context/'+contextId);
    };
    const generateYearOptions = () => {
        const currentYear = new Date().getFullYear();
        const years = [];
        for (let year = currentYear; year >= currentYear - 10; year--) {
            years.push(year);
        }
        return years.map(year => (
            <option key={year} value={year}>
                {year}
            </option>
        ));
    };

    const generateMonthOptions = () => {
        const months = [
            'January', 'February', 'March', 'April', 'May', 'June',
            'July', 'August', 'September', 'October', 'November', 'December'
        ];
        return months.map(month => (
            <option key={month} value={month}>
                {month}
            </option>
        ));
    };

    const generateDayOptions = () => {
        const days = Array.from({ length: 31 }, (_, i) => (i + 1).toString().padStart(2, '0'));
        return days.map(day => (
            <option key={day} value={day}>
                {day}
            </option>
        ));
    };
    const formatDate = (year, month, day) => {
        if (year && month && day) {
            return `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
        }
        return '';
    };

    useEffect(() => {
        const fetchData = () => {
            const dateQuery = formatDate(selectedYear, selectedMonth, selectedDay);
            fetch(`http://localhost:8082/api/chat/sessions/contexts?asc_sort=${sortByDate}&searchNameQuery=${searchTerm}&searchDateQuery=${dateQuery}`)
                .then(response => response.json())
                .then(data => setContexts(data))
                .catch(error => console.error('Error fetching contexts:', error));
        };

        fetchData();
    }, [sortByDate, searchTerm, selectedYear, selectedMonth, selectedDay]);

    return (
        <div className="context-table-page">
            <div className="chat-header">
                YagaTalk
            </div>

            <div className="input-container">

                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Search..."
                        value={searchTerm}
                        onChange={e => setSearchTerm(e.target.value)}
                    />
                </div>

                <div className="sort-dropdown">
                    <label htmlFor="sortSelect">Sort by date:</label>
                    <select
                        id="sortSelect"
                        value={sortByDate ? 'descending' : 'ascending'}
                        onChange={e => setSortByDate(e.target.value === 'descending')}
                    >
                        <option value="descending">Descending date</option>
                        <option value="ascending">Ascending date</option>
                    </select>
                </div>

                <div className="date-dropdown">
                    <select
                        value={selectedDay}
                        onChange={e => setSelectedDay(e.target.value)}
                    >
                        <option value="">Day</option>
                        {generateDayOptions()}
                    </select>
                </div>
                <div className="date-dropdown">
                    <select
                        value={selectedMonth}
                        onChange={e => setSelectedMonth(e.target.value)}
                    >
                        <option value="">Month</option>
                        {generateMonthOptions()}
                    </select>
                </div>
                <div className="date-dropdown">
                    <select
                        value={selectedYear}
                        onChange={e => setSelectedYear(e.target.value)}
                    >
                        <option value="">Year</option>
                        {generateYearOptions()}
                    </select>
                </div>
            </div>

            <div className="context-table">
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Created time</th>
                    </tr>
                    </thead>
                    <tbody>
                    {contexts.map(context => (
                        <tr
                            key={context.name}
                            onClick={() => handleRowClick(context.id)}
                            className="clickable-row"
                        >
                            <td>{context.name}</td>
                            <td>{context.createdTime}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default ContextTable;