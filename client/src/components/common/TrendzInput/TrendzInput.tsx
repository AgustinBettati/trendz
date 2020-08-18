import React from 'react';
import './TrendzInput.css';

type InputProps = {
    value?: string,
    label?: String,
    placeholder?: string,
    disabled?: boolean
    onChange?: any
}

export const TrendzInput = ({value, label, placeholder, disabled, onChange}: InputProps) => (
    <div className={'input-wrapper'}>
        <div className={'input-label'}>{label}</div>
        <input value={value} onChange={onChange} disabled={disabled} placeholder={placeholder}/>
    </div>
)