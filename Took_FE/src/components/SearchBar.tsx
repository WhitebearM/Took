import React, { useState } from "react";
import { TextField, InputAdornment, IconButton } from "@mui/material";
import { FaMagnifyingGlass } from "react-icons/fa6";
import { GoXCircleFill } from "react-icons/go";

// width를 prop으로 받도록 수정
const SearchBar = ({ width = "550px" }: { width?: string }) => {
  const [searchText, setSearchText] = useState("");

  // 입력된 텍스트를 지우는 함수
  const handleClearSearch = () => {
    setSearchText(""); // 입력 항목 리셋
  };

  return (
    <TextField
      variant="outlined"
      placeholder="검색어를 입력하세요"
      value={searchText}
      onChange={(e) => setSearchText(e.target.value)}
      sx={{
        backgroundColor: "#fff",
        borderRadius: "15px",
        width, // 검색창의 너비를 prop으로 받음
        "& .MuiOutlinedInput-root": {
          padding: "5px 15px", // 세로 길이 조정
          height: "45px", // 검색창의 높이 설정
          "& fieldset": {
            border: "none", // 테두리 제거
          },
        },
      }}
      InputProps={{
        startAdornment: (
          <InputAdornment position="start">
            <FaMagnifyingGlass style={{ margin: "0 20px" }} /> {/* 패딩 추가 */}
          </InputAdornment>
        ),
        endAdornment: searchText && (
          <InputAdornment position="end">
            <IconButton onClick={handleClearSearch}>
              <GoXCircleFill
                style={{ margin: "0 10px", fontSize: "1.25rem" }}
              />{" "}
              {/* 패딩 추가 */}
            </IconButton>
          </InputAdornment>
        ),
        inputProps: {
          style: {
            fontSize: "0.95rem", // placeholder 크기 조정 (14px)
          },
        },
      }}
    />
  );
};

export default SearchBar;
