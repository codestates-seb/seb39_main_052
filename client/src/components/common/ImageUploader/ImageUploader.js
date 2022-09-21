import { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import blankImage from "../../../assets/blankImage.webp";
import { addImage, addMainImage, deleteImage, deleteMainImage } from "../../../features/imageSlice";
import { faSpinner, faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, Img, Input, Button, StyledFontAwesomeIcon } from "./ImageUploaderStyle";

const ImageUploader = ({ size, index, mode }) => {

    const [imageUrl, setImageUrl] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const dispatch = useDispatch();
    const imgRef = useRef();

    const imageHandler = async (e) => {
        e.preventDefault();
        setIsLoading(true);

        if (e.target.files[0]) {
            const uploadFile = e.target.files[0]
            setImageUrl(URL.createObjectURL(uploadFile));
            // 메인 사진을 추가하는 경우
            if (mode === `main`) {
                dispatch(addMainImage({ image: uploadFile }))
            }
            // 요리 순서 사진을 추가하는 경우
            if (mode === `steps`) {
                dispatch(addImage({ index: index, image: uploadFile }));
            }
        }
    };

    const handleClick = () => {
        // 버튼 클릭으로 input 클릭과 동일한 기능을 한다.
        imgRef.current.click();
    };

    const handleDelete = () => {
        setImageUrl(null);
        // 메인 사진을 지우는 경우
        if (mode === `main`) {
            dispatch(deleteMainImage());
        }
        //요리 순서 사진을 지우는 경우
        if (mode === `steps`) {
            dispatch(deleteImage({ index: index }));
        }
    }

    return (
        <Container className={size}>
            <Img
                src={imageUrl ? imageUrl : blankImage}
                onLoad={() => setIsLoading(false)}
            />
            {isLoading && <StyledFontAwesomeIcon icon={faSpinner} className="loading" spin />}
            <Input
                type="file"
                accept="image/*"
                name="file"
                ref={imgRef}
                onChange={imageHandler}
            />
            {imageUrl && !isLoading
                ? <StyledFontAwesomeIcon icon={faXmark} className="cancel" onClick={handleDelete} />
                : <StyledFontAwesomeIcon icon={faPlus} className="upload" onClick={handleClick} />}
            {/* {imageUrl && !isLoading
                ? <Button onClick={handleDelete}>이미지 삭제</Button>
                : <Button onClick={handleClick}>이미지 업로드</Button>} */}
        </Container>
    )
};

export default ImageUploader;