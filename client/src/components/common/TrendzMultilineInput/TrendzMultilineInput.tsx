import React from 'react';
import './TrendzMultilineInput.css';


type    MultilineProps = {
    value?: string,
    label?: string,
    placeholder?: string,
    disabled?: boolean,
    password?: boolean,
    onChange?: any,
    onFocus?: any,
    onBlur?: any
}

export const TrendzMultilineInput = (props: MultilineProps) => (
    <div className={'input-wrapper'}>
        <div className={'input-label'}>{props.label}</div>
        <textarea
            onChange={props.onChange}
            disabled={props.disabled}
            placeholder={props.placeholder}
            value={props.value}
            onFocus={props.onFocus}
            onBlur={props.onBlur}
            rows={5}
            cols={77}


        />
    </div>
)