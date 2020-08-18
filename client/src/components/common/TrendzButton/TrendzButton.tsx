import React from 'react';
import './TrendzButton.css';

type ButtonProps = {
    title: string,
    onClick: any
}

export const TrendzButton = ({onClick, title}: ButtonProps) => (
    <button onClick={onClick}>{title}</button>
)