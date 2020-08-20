import React from 'react';
import './TrendzButton.css'

type ButtonProps = {
    title: String,
    disabled?: boolean,
    onClick: any
}

export const TrendzButton = ({title, onClick, disabled}: ButtonProps) => (
    <button onClick={onClick} disabled={disabled}>{title}</button>
)