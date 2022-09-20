import { useState, useRef } from "react";
import { useDispatch, useSelector } from "react-redux";
import axios from "axios";
import blankImage from "../../../assets/blankImage.webp";
import { addImage, addMainImage, deleteImage, deleteMainImage } from "../../../features/imageSlice";
import { faSpinner, faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, Img, Input, Button, StyledFontAwesomeIcon } from "./ImageUploaderStyle";

const ImageUploader = ({ size, index, stepFiles, setStepFiles, mode }) => {

    const [imageUrl, setImageUrl] = useState(null);
    const [isLoading, setIsLoading] = useState(false);

    const dispatch = useDispatch();

    // const stepFiles = useSelector((state) => {
    //     return state.images;
    // });
    // console.log(stepFiles);

    const imgRef = useRef();
    // let tmpArr = [...stepFiles]
    // tmpArr[index] = null;
    // setStepFiles(tmpArr);

    const imageHandler = async (e) => {
        e.preventDefault();
        setIsLoading(true);

        if (e.target.files[0]) {
            const uploadFile = e.target.files[0]
            setImageUrl(URL.createObjectURL(uploadFile));

            if (mode === `main`) {
                dispatch(addMainImage({ image: uploadFile }))
            }
            if (mode === `steps`) {
                dispatch(addImage({ index: index, image: uploadFile }));
            }

            // tmpArr = [...stepFiles]
            // tmpArr[index] = uploadFile;
            // setStepFiles(tmpArr);

            // // // 추후 서버에 데이터 전송 시 formData를 전달 (다른 정보 추가 시 formData.append('name', sth))
            // const formData = new FormData();
            // formData.append('files', stepFiles);

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

        if (mode === `main`) {
            dispatch(deleteMainImage());
        }
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