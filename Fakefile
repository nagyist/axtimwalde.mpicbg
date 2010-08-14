javaVersion=1.5
all <- mpicbg_.jar clahe_.jar

mpicbg.jar <- \
	mpicbg/ij/*.java \
	mpicbg/ij/clahe/*.java \
	mpicbg/ij/stack/*.java \
	mpicbg/ij/util/*.java \
	mpicbg/imagefeatures/*.java \
	mpicbg/models/*.java \
	mpicbg/util/*.java \
	LICENSE

CLASSPATH(mpicbg_.jar)=mpicbg.jar
mpicbg_.jar <- \
	mpicbg/ij/plugin/*.java \
	MOPS_ExtractPointRoi.java \
	SIFT_Align.java \
	SIFT_ExtractPointRoi.java \
	Stack_Rotate.java \
	Transform_Affine.java \
	Transform_MovingLeastSquaresMesh.java \
	Transform_Perspective.java \
	Transform_Rigid.java \
	Transform_Roi.java \
	Transform_Similarity.java \
	plugins.config \
	LICENSE

clahe_.jar <- \
	mpicbg/ij/clahe/FastFlat.java \
	mpicbg/ij/clahe/Flat.java \
	mpicbg/ij/clahe/PlugIn.java \
	mpicbg/ij/clahe/Util.java \
	mpicbg/util/Util.java \
	plugins.config[clahe.config] \
	LICENSE[LICENSE.GPL]