import React from 'react';
import './TrendzInput.css';

type InputProps = {
    label?: String,
    placeholder?: string,
    disabled?: boolean
    onChange: any
}

export const TrendzInput = ({label, placeholder, disabled, onChange}: InputProps) => (
    <div className={'input-wrapper'}>
        <div className={'input-label'}>{label}</div>
        <input onChange={onChange} disabled={disabled} placeholder={placeholder}/>
    </div>
)