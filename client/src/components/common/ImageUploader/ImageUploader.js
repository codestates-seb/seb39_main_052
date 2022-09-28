import { useState, useRef } from "react";
import { useLocation } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import blankImage from "../../../assets/blankImage.webp";
import { addImage, addMainImage, deleteImage, deleteMainImage } from "../../../features/imageSlice";
import { setMainImage, setStepImage, editMainImage, editStepImage, deleteMainImg, deleteStepImage } from "../../../features/recipeSlice";
import { faSpinner, faPlus, faXmark } from "@fortawesome/free-solid-svg-icons";
import { Container, Img, Input, Button, StyledFontAwesomeIcon } from "./ImageUploaderStyle";

const ImageUploader = ({ size, index, mode }) => {

    const [imageUrl, setImageUrl] = useState(null); //미리보기용
    const [isLoading, setIsLoading] = useState(false);

    const { pathname } = useLocation();
    const dispatch = useDispatch();

    const imgRef = useRef(); // input 내장 버튼을 직접 만든 버튼과 연결하기 위한 ref

    // 메인 이미지 url
    const mainImageUrl = useSelector((state) => {
        return state.recipe.imageInfo.imagePath;
    }); 
    // console.log("mainImageUrl", mainImageUrl);
    // 요리 순서 상태
    const steps = useSelector((state) => {
        return state.recipe.steps;
    }); 
    // console.log("steps", steps);

    const imageHandler = (e) => {
        e.preventDefault();
        setIsLoading(true);

        if (e.target.files[0]) {
            const uploadFile = e.target.files[0]

            // 레시피 작성하기
            if (pathname === "/recipes/new") {
                // 메인 사진을 추가하는 경우
                if (mode === `main`) {
                    dispatch(addMainImage({ image: uploadFile }));
                    dispatch(setMainImage({ mainImage: URL.createObjectURL(uploadFile)}));
                }
                // 요리 순서 사진을 추가하는 경우
                if (mode === `steps`) {
                    dispatch(addImage({ index: index, image: uploadFile }));
                    dispatch(setStepImage({ index: index, imagePath: URL.createObjectURL(uploadFile)}))
                }
            }
            // 레시피 수정하기
            else {
                if (mode === `main`) {
                    dispatch(addMainImage({ image: uploadFile }));
                    dispatch(editMainImage({ mainImage: URL.createObjectURL(uploadFile)}));
                }
                // 요리 순서 사진을 추가하는 경우
                if (mode === `steps`) {
                    dispatch(addImage({ index: index, image: uploadFile }));
                    dispatch(editStepImage({ index: index, imagePath: URL.createObjectURL(uploadFile)}))
                }
            }

        }
        else {
            setIsLoading(false);
        }
    };

    const handleClick = async() => {
        // 버튼 클릭으로 input 클릭과 동일한 기능을 한다.
        await imgRef.current.click();
    };

    const handleDelete = () => {
        setImageUrl(null);
        imgRef.current.value="";
        // 메인 사진을 지우는 경우
        if (mode === `main`) {
            dispatch(deleteMainImage());
            dispatch(deleteMainImg());
        }
        //요리 순서 사진을 지우는 경우
        if (mode === `steps`) {
            dispatch(deleteImage({ index: index }));
            dispatch(deleteStepImage({ index: index }))
        }
    }

    return (
        <Container className={size}>
            {mode === `main`
                ? <Img
                    src={mainImageUrl ? mainImageUrl : blankImage}
                    onLoad={() => setIsLoading(false)}
                />
                : <Img
                    src={steps[index].imageInfo.imagePath ? steps[index].imageInfo.imagePath : blankImage}
                    onLoad={() => setIsLoading(false)}
                />
            }
            {isLoading && <StyledFontAwesomeIcon icon={faSpinner} className="loading" spin />}
            <Input
                type="file"
                accept="image/*"
                name="file"
                ref={imgRef}
                onChange={imageHandler}
            />
            {mode === "main"
                ? (mainImageUrl && !isLoading
                    ? <StyledFontAwesomeIcon icon={faXmark} className="cancel" onClick={handleDelete} />
                    : <StyledFontAwesomeIcon icon={faPlus} className="upload" onClick={handleClick} />
                )
                : (steps[index].imageInfo.imagePath && !isLoading
                    ? <StyledFontAwesomeIcon icon={faXmark} className="cancel" onClick={handleDelete} />
                    : <StyledFontAwesomeIcon icon={faPlus} className="upload" onClick={handleClick} />
                )
            }
        </Container>
    )
};

export default ImageUploader;