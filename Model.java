public class Model
{
   private String name="";
   private String description="";
   private String course="";
   private String campus="";

     public Model()
	{
	}

	public Model(String name,String description,String course,String campus)
	{
		this.name=name;
		this.description=description;
		this.course=course;
		this.campus=campus;
	}
	public String getName(){ return name;}
	public String getDescription(){ return description;}
	public String getCourse(){return course;}
    public String getCampus(){return campus;}

    public String toString()
	{
		return name+"\t"+description+"\t"+course+"\t"+campus;
	}

}
