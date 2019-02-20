package com.hearatale.sightwords.data.model.phonics.piece;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataPieceModel {
    @SerializedName("cols")
    @Expose
    private int cols;
    @SerializedName("pieces")
    @Expose
    private PiecesModel pieces;
    @SerializedName("pixelsTall")
    @Expose
    private int pixelsTall;
    @SerializedName("rows")
    @Expose
    private int rows;
    @SerializedName("pixelsWide")
    @Expose
    private int pixelsWide;

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public PiecesModel getPieces() {
        return pieces;
    }

    public void setPieces(PiecesModel pieces) {
        this.pieces = pieces;
    }

    public int getPixelsTall() {
        return pixelsTall;
    }

    public void setPixelsTall(int pixelsTall) {
        this.pixelsTall = pixelsTall;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPixelsWide() {
        return pixelsWide;
    }

    public void setPixelsWide(int pixelsWide) {
        this.pixelsWide = pixelsWide;
    }
}
