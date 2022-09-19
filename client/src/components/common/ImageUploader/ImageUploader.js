import { useState, useRef } from "react";
import axios from "axios";
import blankImage from "../../../assets/blankImage.webp";
import { faSpinner, faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container ,Img, Input, Button, StyledFontAwesomeIcon } from "./ImageUploaderStyle";

const ImageUploader = ( {imgPostApi, size} ) => {

    const [imageUrl, setImageUrl] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const imgRef = useRef();

    const imageHandler = async(e) => {
        e.preventDefault();
        setIsLoading(true);
        
        if (e.target.files[0]) {
            const uploadFile = e.target.files[0]
            setImageUrl(URL.createObjectURL(uploadFile));
    
            // // 추후 서버에 데이터 전송 시 formData를 전달 (다른 정보 추가 시 formData.append('name', sth))
            // const formData = new FormData();
            // formData.append('file', uploadFile);
            
            // await axios({
            //     method: 'post',
            //     url: imgPostApi,
            //     data: formData,
            //     headers: {
            //         'Content-Type': 'multipart/form-data',
            //     }
            // })
        }
    };

    const handleClick = () => {
        //버튼 클릭으로 input 클릭과 동일한 기능을 한다.
        imgRef.current.click();
    };

    const handleDelete = () => {
        setImageUrl(null);
        //서버에 보내는 formData도 추후 변경 필요함, 지금은 보여지는 것만 삭제
    }

    return (
        <Container className={size}>
            <Img
                src={imageUrl? imageUrl : blankImage}
                onLoad={() => setIsLoading(false)}
            />
            {isLoading && <StyledFontAwesomeIcon icon={faSpinner} className="loading" spin/>}
            <Input 
                type="file"
                accept="image/*"
                name="file"
                ref={imgRef}
                onChange={imageHandler}
            />
            {imageUrl && !isLoading
                ? <StyledFontAwesomeIcon icon={faXmark} className="cancel" onClick={handleDelete} />
                : <StyledFontAwesomeIcon icon={faPlus} className="upload" onClick={handleClick}/>}
            {/* {imageUrl && !isLoading
                ? <Button onClick={handleDelete}>이미지 삭제</Button>
                : <Button onClick={handleClick}>이미지 업로드</Button>} */}
        </Container>
    )
};

export default ImageUploader;