import React from 'react';
import {MdDone} from 'react-icons/md';
import './Checkbox.css'

type CheckboxProps = {
    handleChange: () => void,
    selected: boolean
}

const Checkbox = ({handleChange, selected}: CheckboxProps) => {

    return(
        <div className={'checkbox'} onClick={() => handleChange()}>
            {selected && <MdDone size={15} color={'black'}/>}
        </div>
    )
}

export default Checkbox;