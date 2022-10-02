import styled from "styled-components";

export const ProfileWrapper = styled.div`
  /* padding: 16px; */
  display: flex;
  flex-direction: column;
  align-items: center;

  > h2 {
    padding-bottom: 30px;
  }
  > div.cancel_done_button_wrapper {
    display: flex;
    width: 100%;
    justify-content: space-around;
    padding-top: 16px;
  }
`;
export const ProfilePhoto = styled.img`
  width: 100px;
  height: 100px;
  border-radius: 50%;
`;
export const ProfileName = styled.div`
  padding: 16px 0;
`;
