
import React from 'react';
import './TrendzButton.css'

type ButtonProps = {
    title: string,
    disabled?: boolean,
    onClick: () => any
}

export const TrendzButton = (props: ButtonProps) => (
    <button className={props.disabled ? 'disabled' : 'enabled'} onClick={props.onClick} disabled={props.disabled}>{props.title}</button>
)