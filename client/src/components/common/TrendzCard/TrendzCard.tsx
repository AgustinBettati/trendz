import React from 'react';
import './TrendzInput.css';
import logo from "../../../assets/TrendzLogo.png";

type CardProps = {
    title?: string,
    height: number,
}

export const TrendzCard = (props: CardProps) => (
    <div className={"main-container"}>
        <div className={'card'}>
            <div className={'header'}>
                <img className={'trendz-logo'} src={logo} alt={''}/>
                <div className={'divisor'}/>
                <div className={'title'}> props.title</div>
                <div className={'body'}>
                    height={props.height}
                </div>
            </div>
        </div>
    </div>





    /* <div className={'input-wrapper'}>
         <div className={'input-label'}>{props.label}</div>
         <input
             onChange={props.onChange}
             disabled={props.disabled}
             placeholder={props.placeholder}
             type={props.password ? 'password' : 'text'}
             value={props.value}
             onFocus={props.onFocus}
             onBlur={props.onBlur}
         />
     </div>*/
)