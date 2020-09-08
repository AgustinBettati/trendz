import React from 'react';
import './TrendzLongInput.css';

type InputProps = {
    value?: string,
    label?: string,
    placeholder?: string,
    disabled?: boolean,
    onChange?: any,
    onFocus?: any,
    onBlur?: any
}

export const TrendzLongInput = (props: InputProps) => (
    <div className={'input-wrapper'}>
        <div className={'input-label'}>{props.label}</div>
        <input
            onChange={props.onChange}
            disabled={props.disabled}
            placeholder={props.placeholder}
            value={props.value}
            onFocus={props.onFocus}
            onBlur={props.onBlur}
        />
    </div>
)